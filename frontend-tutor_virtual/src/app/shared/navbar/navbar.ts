import { Component } from '@angular/core';
import { Location, CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.scss'],
  imports: [RouterModule, CommonModule]
})
export class Navbar {

  constructor(
    private location: Location,
    private router: Router
  ) {}

  goBack(): void {
    this.location.back();
  }

  // ✅ Muestra la flecha solo si NO estás en la página principal
  mostrarFlecha(): boolean {
    const ruta = this.router.url;
    return ruta !== '/' && ruta !== '';
  }

  // ✅ CORREGIDO: Muestra el botón "Administrador" SOLO en la página principal
 mostrarBotonAdmin(): boolean {
  const ruta = this.router.url;
  console.log('🔍 Ruta actual:', ruta);
  
  if (ruta.includes('admin-login') || 
      ruta.includes('admin-register') || 
      ruta.includes('admin-dashboard')) {
    console.log('❌ Ocultando botón admin');
    return false;
  }
  
  const mostrar = ruta === '/' || ruta === '';
  console.log('✅ Mostrar botón admin:', mostrar);
  return mostrar;
}

  // ✅ NUEVO: Verifica si estás en páginas de admin (login/register/dashboard)
  esRutaAdmin(): boolean {
    const ruta = this.router.url;
    return ruta.includes('/admin-login') || 
           ruta.includes('/admin-register') || 
           ruta.includes('/admin-dashboard');
  }
}