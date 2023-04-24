self.addEventListener("activate", function (event) {
  console.log("service worker activated");
});

self.addEventListener("push", async function (event) {
  const message = await event.data?.json();
  const { title, body } = message;
  console.log(this);

  event.waitUntil(
    this.registration.showNotification(title, {
      body,
    })
  );
});
