import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder} from '@angular/forms';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {

  signUpForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.signUpForm = fb.group({
      'userName' : [],
      'password' : [],
      'passwordRepeat': [],
      'email' : [],
      'firstName': [],
      'lastName': []

    });
  }

  ngOnInit() {


  }

  addPost() {
    console.log(this.signUpForm.value);
    const userName = this.signUpForm.value.userName;
    const psw = this.signUpForm.value.password;
    const pswRepeat = this.signUpForm.value.passwordRepeat;
    const email = this.signUpForm.value.email;
    const firstName = this.signUpForm.value.firstName;
    const lastName = this.signUpForm.value.lastName;
  }



}
