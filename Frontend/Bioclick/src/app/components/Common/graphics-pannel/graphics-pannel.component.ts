import { Component } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { AdminService } from '../../../services/admins.service';
import Chart from 'chart.js/auto'
import { Contenido } from '../../../models/user/graphics/products-more-valorated.interface';
import { usuariosEncontrados } from '../../../models/user/graphics/users-with-more-valorations.interface';
@Component({
  selector: 'app-graphics-pannel',
  templateUrl: './graphics-pannel.component.html',
  styleUrl: './graphics-pannel.component.scss',
})
export class GraphicsPannelComponent {
  name: string = '';
  isExpanded: boolean = true;
  porcentajeUsuariosHabilitados: number = 0;
  totalProductos: number = 0;
  totalCo02: number = 0;
  usuariosVerificados: Chart | undefined;
  topUsuariosConMasValoraciones: Chart | undefined;
  topProductosMejorValorados: Chart | undefined;
  constructor(private userService: UserService, private adminService: AdminService) { }

  ngOnInit(): void {
    this.getDatas();
  }

  getDatas() {
    this.adminService.getTotalUsersValidated().subscribe((value: number) => {
      this.porcentajeUsuariosHabilitados = Number(value.toFixed(2));;
      const dataUsuariosValidados = {
        datasets: [{
          label: "Usuarios",
          data: [value, 100 - value],
          fill: false,
          borderColor: ['rgb(0, 148, 32)', 'rgba(0, 0, 0, 0)'],
          backgroundColor: ['rgb(0, 148, 32)', 'rgba(0, 0, 0, 0)'],
          tension: 0.1,
          cutout: '90%'
        }]
      };
      this.usuariosVerificados = new Chart("usuariosVerificados", {
        type: 'pie',
        data: dataUsuariosValidados,
      });
    });
    this.adminService.getTotalC02().subscribe((value: number) => {
      this.totalCo02 = Number(value.toFixed(2));
    });
    this.adminService.getTotalProducts().subscribe((value: number) => {
      this.totalProductos = value;
    });
    this.adminService.getProductsMoreValorated().subscribe((value) => {
      // Prepare chart data
      const chartData = {
        labels: value.contenido.map((item: Contenido) => item.nombreProducto),
        datasets: [{
          label: "Productos mejor valorados",
          data: value.contenido.map((item: Contenido) => item.mediaPuntuacion),
          backgroundColor: [
  'rgb(76, 201, 91)',
  'rgb(0, 168, 150)',
  'rgb(0, 148, 32)',
  'rgb(72, 202, 228)',
  'rgb(34, 139, 230)'
          ]
        }],
        cutout: '40%'

      };
      this.topProductosMejorValorados = new Chart("topProductosMejorValorados", {
        type: 'pie',
        data: chartData,
      });
    });
    this.adminService.getUsersWithMoreValorations().subscribe((value) => {
      // Prepare chart data
      const chartData = {
        labels: value.contenido.map((item: usuariosEncontrados) => item.username),
        datasets: [{
          label: "Usuarios con más valoraciones",
          data: value.contenido.map((item: usuariosEncontrados) => item.valoracionesTotales),
          backgroundColor: [
  'rgb(76, 201, 91)',
  'rgb(0, 168, 150)',
  'rgb(0, 148, 32)',
  'rgb(72, 202, 228)',
  'rgb(34, 139, 230)'
          ]
        }],
        cutout: '40%'
      };
      this.topUsuariosConMasValoraciones = new Chart("topUsuariosConMasValoraciones", {
        type: 'pie',
        data: chartData,
      });
    });
  }

  handleSidebarToggle() {
    this.isExpanded = !this.isExpanded;
  }
}
