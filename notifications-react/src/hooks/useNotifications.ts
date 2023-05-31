import axios from "axios";
import {
  NotificationsStatus,
  PublicKey,
} from "../interfaces/notificationInterfaces";
import { useEffect, useState } from "react";

const notificationsServer = axios.create({
  baseURL: "http://localhost:8080",
});

async function regSw() {
  console.log({ navigator });
  if ("serviceWorker" in navigator) {
    const url = "/sw.js";
    const reg = await navigator.serviceWorker.register(url, { scope: "/" });
    console.log("service config is", { reg });
    return reg;
  }
  console.log("Error in sw registration");

  throw Error("serviceworker not supported");
}

export const useNotificacions = () => {
  const [publicKey, setPublicKey] = useState("");
  const [swReg, setSwReg] = useState<ServiceWorkerRegistration>();
  const [notificationStatus, setNotificationStatus] =
    useState<NotificationsStatus>(NotificationsStatus.UNDEFINED);

  const isStatusOK = (status: number): boolean =>
    status >= 200 && status <= 299;

  useEffect(() => {
    regSw()
      .then(async (reg) => {
        setSwReg(reg);
      })
      .catch(() => {
        setNotificationStatus(NotificationsStatus.NOT_SUPPORTED);
      });
  }, []);

  async function requestPermission() {
    const permission = await Notification.requestPermission();
    console.log({ permission });

    if (permission === "granted") {
      setNotificationStatus(NotificationsStatus.UNSUBSCRIBED);
      await getPublicKey();
    } else {
      setNotificationStatus(NotificationsStatus.DISABLED);
    }
  }

  async function getPublicKey() {
    const resp = await notificationsServer.get<PublicKey>("/publicKey");
    if (isStatusOK(resp.status)) {
      setPublicKey(resp.data.key);
    } else {
      throw Error("Failed to get key");
    }
  }

  async function subscribe() {
    if (swReg) {
      let subscription = await swReg.pushManager.getSubscription();
      console.log({ subscription });

      if (subscription === null) {
        subscription = await swReg.pushManager.subscribe({
          userVisibleOnly: true,
          applicationServerKey: publicKey,
        });
      }

      if (subscription) {
        const resp = await notificationsServer.post("/subscribe", subscription);
        console.log(resp);
        if (isStatusOK(resp.status)) {
          setNotificationStatus(NotificationsStatus.SUBSCRIBED);
        }
      }
    } else {
      throw Error("Service Worker not registered");
    }
  }

  async function unsubscribe() {
    if (swReg) {
      const subscription = await swReg.pushManager.getSubscription();
      console.log({ subscription });

      if (subscription !== null) {
        const resp = await notificationsServer.post(
          "/unsubscribe",
          subscription.endpoint
        );
        console.log(resp);
        if (isStatusOK(resp.status)) {
          setNotificationStatus(NotificationsStatus.UNSUBSCRIBED);
        }
      }
    } else {
      throw Error("Service Worker not registered");
    }
  }

  async function sendNotification() {
    const resp = await notificationsServer.get("/sendNotification");
    console.log(resp);
  }

  return {
    notificationStatus,
    requestPermission,
    subscribe,
    unsubscribe,
    sendNotification,
  };
};
