import { Component } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-verify-account',
  templateUrl: './verify-account.component.html',
  styleUrl: './verify-account.component.css'
})
export class VerifyAccountComponent {
  randomBgNumber = Math.floor(Math.random() * 4) + 1;
  code1: string = '';
  code2: string = '';
  code3: string = '';
  code4: string = '';
  code5: string = '';
  code6: string = '';

  showError: boolean = false;

  constructor(private userService: UserService, private router: Router) { }

  verifyUser() {
    const codigo = this.code1 + this.code2 + this.code3 + this.code4 + this.code5 + this.code6;
    this.userService.verifyUser(codigo).subscribe({
      next: () => {
        this.showError = false;
        this.router.navigate(['/login']);
      },
      error: () => {
        this.showError = true;
      }
    });
  }
  autoFocusNext(event: any, nextInput: HTMLInputElement | null) {
    const value = event.target.value;
    // Si no es número, borra el valor y no avanza el foco
    if (!/^\d$/.test(value)) {
      event.target.value = '';
      return;
    }
    if (value.length === 1 && nextInput) {
      nextInput.focus();
    }
  }

  autoFocusPrev(event: KeyboardEvent, prevInput: HTMLInputElement | null) {
    if (event.key === 'Backspace' && !(event.target as HTMLInputElement).value && prevInput) {
      prevInput.focus();
    }
  }
  onPaste(event: ClipboardEvent) {
    const pastedData = event.clipboardData?.getData('text') || '';
    if (pastedData.length === 6 && /^\d+$/.test(pastedData)) {
      this.code1 = pastedData[0];
      this.code2 = pastedData[1];
      this.code3 = pastedData[2];
      this.code4 = pastedData[3];
      this.code5 = pastedData[4];
      this.code6 = pastedData[5];
      event.preventDefault();
    }
  }
}
