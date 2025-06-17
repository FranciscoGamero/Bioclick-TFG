import { Component, EventEmitter, Input, Output } from "@angular/core";
import { Router } from "@angular/router";

@Component({
  selector: "my-sidebar",
  templateUrl: "./sidebar.component.html",
  styleUrls: ["./sidebar.component.scss"]
})
export class SidebarComponent {
  @Input() isExpanded: boolean = true;
  @Output() toggleSidebar = new EventEmitter<void>();
  isUserOpen: boolean = false;
  isProductsOpen: boolean = false;
  constructor(private router: Router) {}
  handleSidebarToggle = () => {
    this.toggleSidebar.emit();
    this.isUserOpen = false;
    this.isProductsOpen = false;
  };

  toggleUserDropdown(event: Event) {
    event.preventDefault();
    this.isUserOpen = !this.isUserOpen;
  }
  toggleProductsDropdown(event: Event) {
    event.preventDefault();
    this.isProductsOpen = !this.isProductsOpen;
  }

  isAdmin(): boolean {
    const role = localStorage.getItem("role");
    return role === "ROLE_ADMIN";
  }
    logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}
