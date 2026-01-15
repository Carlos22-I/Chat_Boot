// src/app/pipes/text-format.pipe.ts
import { Pipe, PipeTransform } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Pipe({
  name: 'textFormat',
  standalone: true
})
export class TextFormatPipe implements PipeTransform {

  constructor(private sanitizer: DomSanitizer) {}

  transform(text: string): SafeHtml {
    if (!text) return '';

    // Convertir saltos de línea a <br>
    let formatted = text.replace(/\n/g, '<br>');

    // Hacer negritas para títulos tipo "PASO 1:", "📋 PASO 2:", etc.
    formatted = formatted.replace(/(📋\s*PASO\s*\d+:|PASO\s*\d+:)/gi, '<strong>$1</strong>');

    // Hacer negritas para secciones importantes
    formatted = formatted.replace(/(💡\s*[^:]+:)/gi, '<strong>$1</strong>');

    // Sanitizar el HTML
    return this.sanitizer.bypassSecurityTrustHtml(formatted);
  }
}