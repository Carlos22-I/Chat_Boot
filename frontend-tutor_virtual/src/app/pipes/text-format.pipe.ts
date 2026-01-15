// src/app/pipes/text-format.pipe.ts
import { Pipe, PipeTransform } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Pipe({
  name: 'textFormat',
  standalone: true
})
export class TextFormatPipe implements PipeTransform {

  constructor(private sanitizer: DomSanitizer) { }

  transform(text: string): SafeHtml {
    if (!text) return '';

    let formatted = text;

    // 1. Procesar negritas Markdown: **texto** -> <strong>texto</strong>
    formatted = formatted.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');

    // 2. Procesar viñetas: líneas que empiezan con * -> viñeta con guion o punto
    // Buscamos líneas que empiecen con asterisco (con o sin espacio inicial)
    formatted = formatted.replace(/^\s*\*\s+(.*)$/gm, '• $1');

    // 3. Convertir saltos de línea a <br>
    formatted = formatted.replace(/\n/g, '<br>');

    // 4. Hacer negritas para títulos tipo "PASO 1:", etc. (por si no tienen **)
    formatted = formatted.replace(/(PASO\s*\d+:)/gi, '<strong>$1</strong>');

    // Sanitizar el HTML
    return this.sanitizer.bypassSecurityTrustHtml(formatted);
  }
}