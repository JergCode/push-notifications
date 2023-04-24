import { useNotificacions } from "../hooks/useNotifications";
import { NotificationsStatus } from "../interfaces/notificationInterfaces";
import NotificationDisabled from "./components/NotificationDisabled";
import { NotificationRequest } from "./components/NotificationRequest";
import { NotificationSubscribed } from "./components/NotificationSubscribed";
import NotificationUnsubscribed from "./components/NotificationUnsubscribed";
import { NotificationsNoSupported } from "./components/NotificationsSupport";

export const HomePage = () => {
  const {
    notificationStatus,
    subscribe,
    unsubscribe,
    sendNotification,
    requestPermission,
  } = useNotificacions();

  const notificationManagment = () => {
    switch (notificationStatus) {
      case NotificationsStatus.UNDEFINED:
        return <NotificationRequest requestPermission={requestPermission} />;
      case NotificationsStatus.NOT_SUPPORTED:
        return <NotificationsNoSupported />;
      case NotificationsStatus.DISABLED:
        return <NotificationDisabled />;
      case NotificationsStatus.SUBSCRIBED:
        return (
          <NotificationUnsubscribed
            onSendNotification={sendNotification}
            onUnsubscribed={unsubscribe}
          />
        );
      case NotificationsStatus.UNSUBSCRIBED:
        return <NotificationSubscribed onSubscribed={subscribe} />;
    }
  };

  return (
    <>
      <h1>Push Notifications Test Page</h1>
      {notificationManagment()}
    </>
  );
};
