import { Component, EventEmitter, Input, Output } from "@angular/core";

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
}
