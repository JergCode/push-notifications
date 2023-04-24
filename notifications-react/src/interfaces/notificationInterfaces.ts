export interface PublicKey {
  key: string;
}

export enum NotificationsStatus {
  UNDEFINED,
  NOT_SUPPORTED,
  DISABLED,
  UNSUBSCRIBED,
  SUBSCRIBED,
}
