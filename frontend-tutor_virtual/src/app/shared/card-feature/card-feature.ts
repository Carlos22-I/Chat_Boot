import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common'; // <-- Importa esto

@Component({
  selector: 'app-card-feature',
  templateUrl: './card-feature.html',
  styleUrls: ['./card-feature.scss'],
  standalone: true, // <-- Marca el componente como standalone
  imports: [CommonModule] // <-- Añade CommonModule a los imports
})
export class CardFeature {
  @Input() title: string = '';
  @Input() subtitle: string = '';
  @Input() iconUrl: string = '';
  @Input() buttonText: string = '';
}
