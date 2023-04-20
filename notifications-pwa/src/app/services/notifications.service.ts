import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

export interface PublicKey {
  key: string;
}

@Injectable({
  providedIn: 'root',
})
export class NotificationsService {
  unit8Key?: Uint8Array;
  key: string = '';
  constructor(private httpClient: HttpClient) {
    this.getPublicKey();
  }

  async getPublicKey(): Promise<void> {
    const headers: HttpHeaders = new HttpHeaders().set('Accept', '*/*');

    this.httpClient
      .get<PublicKey>('http://localhost:8080/publicKey', {
        headers,
      })
      .subscribe({
        next: (resp) => {
          this.key = resp.key;
          this.unit8Key = this.urlB64ToUint8Array(resp.key);
          console.log(this.unit8Key);
        },
        error: (error) => {
          console.log(error);
        },
      });
  }

  subscribe(subscription: PushSubscription): void {
    this.httpClient
      .post('http://localhost:8080/subscribe', subscription.toJSON())
      .subscribe({
        next: (resp) => {
          console.log('Subscribed');
          console.log(resp);
        },
        error: (e) => {
          console.log('Error');
          console.log(e);
        },
      });
  }

  private urlB64ToUint8Array(base64String: string) {
    const padding = '='.repeat((4 - (base64String.length % 4)) % 4);
    const base64 = (base64String + padding)
      .replace(/\-/g, '+')
      .replace(/_/g, '/');
    const rawData = window.atob(base64);
    const outputArray = new Uint8Array(rawData.length);
    for (let i = 0; i < rawData.length; ++i) {
      outputArray[i] = rawData.charCodeAt(i);
    }
    return outputArray;
  }
}
