import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { AllUsersResponse } from '../../../models/user/get-all-users-interface';


@Component({
  selector: 'app-user-pannel',
  templateUrl: './user-pannel.component.html',
  styleUrls: ['./user-pannel.component.scss']
})
export class UserPannelComponent implements OnInit {
  isExpanded: boolean = true;
  name: string = '';
  usersFound: AllUsersResponse | undefined = undefined;
  page: number = 1;
  ngOnInit(): void {
    this.getUsers();
  }

  constructor(private userService: UserService) { }

limpiarUrlFoto(url: string | undefined | null): string {
  const prefix = "http://localhost:8080/download/";
  if (!url) {
    return '';
  }
  if (url.startsWith(prefix)) {
    return url.substring(prefix.length);
  }
  return url;
}
  getUsers() {
    this.userService.getAllUsers(this.page-1).subscribe({
      next: (response) => {
        this.usersFound = response;
        console.log(JSON.stringify(this.usersFound?.contenido[0]));

      },
      error: (error) => {
        console.error('Error fetching users:', error);
      }
    });
  }

  handleSidebarToggle() {
    this.isExpanded = !this.isExpanded;
  }
  onCardClick(userId: string) {
    console.log('Card clicked for user ID:', userId);
  }
}
