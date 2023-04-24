interface Props {
  onSubscribed: () => void;
}

export const NotificationSubscribed = ({ onSubscribed }: Props) => {
  return <button onClick={onSubscribed}>subscribe</button>;
};
