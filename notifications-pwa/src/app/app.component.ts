import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { NotificationsService } from './services/notifications.service';
import { SwPush } from '@angular/service-worker';

enum NotificationStatus {
  NOT_SUPPORTED = 'NOT_SUPPORTED',
  ENABLED = 'ENABLED',
  DISABLED = 'DISABLED',
}
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  key: any;
  isSubscribed = false;
  notStatus = NotificationStatus;
  notificationsStatus: NotificationStatus = NotificationStatus.DISABLED;

  constructor(
    private swPush: SwPush,
    private notificationsService: NotificationsService
  ) {}

  ngOnInit(): void {}

  subscribe() {
    this.key = this.notificationsService.key;
    this.swPush
      .requestSubscription({
        serverPublicKey: this.notificationsService.key,
      })
      .then((subscription) => {
        console.log(subscription);

        this.notificationsService.subscribe(subscription);
        this.isSubscribed = true;
      })
      .catch(console.error);
  }

  unsubscribe() {}
}
