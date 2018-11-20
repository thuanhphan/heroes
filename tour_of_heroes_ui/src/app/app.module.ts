import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { DashboardComponent } from './views/dashboard/dashboard.component';
import { HeroDetailComponent } from './views/hero-detail/hero-detail.component';
import { HeroesComponent } from './views/heroes/heroes.component';
import { MessagesComponent } from './views/messages/messages.component';

import { AppRoutingModule } from './app-routing.module';
import { SignUpComponent } from './views/sign-up/sign-up.component';
import { SignInComponent } from './views/sign-in/sign-in.component';

import { ReactiveFormsModule } from '@angular/forms';
import { ApiProvider } from './provider/api';
import { UserProvider } from './provider/user';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  declarations: [
    AppComponent,
    DashboardComponent,
    HeroesComponent,
    HeroDetailComponent,
    MessagesComponent,
    SignUpComponent,
    SignInComponent,

  ],
  providers: [ApiProvider, UserProvider],
  bootstrap: [AppComponent]
})
export class AppModule { }
