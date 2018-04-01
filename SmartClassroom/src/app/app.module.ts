import { NgModule, ErrorHandler, LOCALE_ID } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import localePt from '@angular/common/locales/pt';
import { IonicApp, IonicModule, IonicErrorHandler } from 'ionic-angular';
import { MyApp } from './app.component';

import { AboutPage } from '../pages/about/about';
import { HomePage, ModalAdicionarAmbiente } from '../pages/home/home';
import { TabsPage } from '../pages/tabs/tabs';

import { MqttConnectionProvider } from '../providers/mqtt-connection/mqtt-connection';

import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { UniqueDeviceID } from '@ionic-native/unique-device-id';
import { IonicStorageModule } from '@ionic/storage';

registerLocaleData(localePt);

@NgModule({
  declarations: [
    MyApp,
    AboutPage,
    HomePage,
    TabsPage,
    ModalAdicionarAmbiente
  ],
  imports: [
    BrowserModule,
    IonicModule.forRoot(MyApp),
    IonicStorageModule.forRoot()
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    AboutPage,
    HomePage,
    TabsPage,
    ModalAdicionarAmbiente
  ],
  providers: [
    StatusBar,
    SplashScreen,
	UniqueDeviceID,
	MqttConnectionProvider,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
    {provide: LOCALE_ID, useValue: 'pt-BR'}
  ]
})
export class AppModule {}
