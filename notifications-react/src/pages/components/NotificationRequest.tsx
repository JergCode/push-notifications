interface Props {
  requestPermission: () => void;
}

export const NotificationRequest = ({ requestPermission }: Props) => {
  return <button onClick={requestPermission}>Request Permission</button>;
};
