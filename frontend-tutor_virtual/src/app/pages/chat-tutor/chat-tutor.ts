import { Component, ViewChild, ElementRef } from '@angular/core';
import { Footer } from "../../shared/footer/footer";
import { Navbar } from "../../shared/navbar/navbar";
import { ChatMessage } from '../models/chat-message.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatService } from '../../services/chat.service';
import { TextFormatPipe } from '../../pipes/text-format.pipe';

@Component({
  selector: 'app-chat-tutor',
  imports: [Footer, Navbar, CommonModule, FormsModule, TextFormatPipe],
  templateUrl: './chat-tutor.html',
  styleUrl: './chat-tutor.scss',
  standalone: true,
})
export class ChatTutor {

  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;

  messages: ChatMessage[] = [];
  newMessageText = '';
  cargando = false;

  constructor(private chatService: ChatService) {}

  onEnterPress(event: any): void {
    if (!event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }

  sendMessage(): void {
    if (!this.newMessageText.trim()) return;

    this.messages.push({
      text: this.newMessageText,
      sender: 'user',
      timestamp: new Date()
    });

    const pregunta = this.newMessageText;
    this.newMessageText = '';
    this.cargando = true;
    
    setTimeout(() => this.scrollToBottom(), 50);

    this.chatService.preguntar(pregunta).subscribe({
      next: (res: { respuesta: string }) => {
        this.messages.push({
          text: res.respuesta,
          sender: 'bot',
          timestamp: new Date()
        });
        this.cargando = false;
      },
      error: (error: any) => {
        this.messages.push({
          text: 'Error al conectar con el servidor',
          sender: 'bot',
          timestamp: new Date()
        });
        this.cargando = false;
      }
    });
  }

  private scrollToBottom(): void {
    try {
      const container = this.messagesContainer.nativeElement;
      container.scrollTop = container.scrollHeight;
    } catch (err) {
      console.error('Error al hacer scroll:', err);
    }
  }

  //  CORREGIDO: Usar el endpoint /descargar-conversacion con el formato correcto
  descargarConversacionCompleta(): void {
    if (this.messages.length === 0) {
      alert('No hay mensajes para descargar.');
      return;
    }

    // Convertir mensajes al formato esperado por el backend
    const mensajesParaPDF = this.messages.map(msg => ({
      tipo: msg.sender === 'user' ? 'consulta' : 'respuesta',
      texto: msg.text
    }));

    console.log('📄 Enviando', mensajesParaPDF.length, 'mensajes al backend');

    //  Llamar al nuevo endpoint
    this.chatService.descargarConversacionCompleta(mensajesParaPDF)
      .subscribe({
        next: (blob: Blob) => {
          console.log('✅ PDF generado, tamaño:', blob.size, 'bytes');
          
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = `conversacion-unamba-${Date.now()}.pdf`;
          document.body.appendChild(a);
          a.click();
          document.body.removeChild(a);
          window.URL.revokeObjectURL(url);
          
          const totalConsultas = this.messages.filter(m => m.sender === 'user').length;
          alert(`✅ PDF descargado con ${totalConsultas} consultas y respuestas`);
        },
        error: (error: any) => {
          console.error('❌ Error al generar PDF:', error);
          alert('Error al generar el PDF. Revisa la consola para más detalles.');
        }
      });
  }
}