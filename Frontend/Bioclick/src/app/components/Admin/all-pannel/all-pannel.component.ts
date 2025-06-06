import { Component, OnInit } from '@angular/core';
import { AllFoundResponse } from '../../../models/user/get-all-found';
import { AdminService } from '../../../services/admins.service';

@Component({
  selector: 'app-all-pannel',
  templateUrl: './all-pannel.component.html',
  styleUrl: './all-pannel.component.scss'
})
export class AllPannelComponent implements OnInit {
  isExpanded: boolean = true;
  name: string = '';
  allFound: AllFoundResponse | undefined = undefined;
  page: number = 1;
  ngOnInit(): void {
    this.foundAll();
  }
  constructor(private adminService: AdminService) { }

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
  foundAll() {
    this.adminService.getAll(this.page-1).subscribe({
      next: (response) => {
        this.allFound = response;
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
