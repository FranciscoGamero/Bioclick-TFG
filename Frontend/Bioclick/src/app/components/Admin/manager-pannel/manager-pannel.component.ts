import { Component } from '@angular/core';
import { AllManagersResponse } from '../../../models/user/get-all-managers-interface';
import { ManagerService } from '../../../services/manager.service';

@Component({
  selector: 'app-manager-pannel',
  templateUrl: './manager-pannel.component.html',
  styleUrl: './manager-pannel.component.scss'
})
export class ManagerPannelComponent {
  isExpanded: boolean = true;
  name: string = '';
  managersFound: AllManagersResponse | undefined = undefined;
  page: number = 1;
  ngOnInit(): void {
    this.getManagers();
  }
  constructor(private managerService: ManagerService) { }

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
  getManagers() {
    this.managerService.getAllManagers(this.page-1).subscribe({
      next: (response) => {
        this.managersFound = response;
        console.log(JSON.stringify(this.managersFound?.contenido[0]));

      },
      error: (error) => {
        console.error('Error fetching managers:', error);
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
