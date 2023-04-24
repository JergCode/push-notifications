interface Props {
  onSendNotification: () => void;
  onUnsubscribed: () => void;
}

export default function NotificationUnsubscribed({
  onSendNotification,
  onUnsubscribed,
}: Props) {
  return (
    <div className="btn-group">
      <button onClick={onSendNotification}>Send Notification</button>
      <button onClick={onUnsubscribed}>Unsubscribe</button>
    </div>
  );
}
