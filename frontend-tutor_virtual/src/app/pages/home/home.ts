import { Component } from '@angular/core';
import { CardFeature } from "../../shared/card-feature/card-feature";
import { RouterModule } from '@angular/router';
import { CommonModule, Location } from '@angular/common';
import { Navbar } from "../../shared/navbar/navbar";
import { Footer } from "../../shared/footer/footer";

@Component({
  selector: 'app-home',
  standalone: true, /*Nuevo creado* */
  imports: [RouterModule, CommonModule, CardFeature, Navbar, Footer],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {
    // Inyecta el servicio Location en el constructor
  constructor(private location: Location) {}

  // Función que se llama cuando se hace clic en el botón
  goBack(): void {
    this.location.back();
  }
}
