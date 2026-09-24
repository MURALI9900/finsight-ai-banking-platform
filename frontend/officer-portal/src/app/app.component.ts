import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';

type Loan = {
  loanId: string; customerId: string; customerName: string;
  outstandingAmount: number; daysOverdue: number; status: string;
};
type AiResponse = {
  answer: string; intent: string; toolUsed: string; data: unknown[]; sources: string[];
};

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  private readonly gateway = 'http://localhost:8080';
  username = '';
  password = '';
  token = '';
  question = '';
  loans: Loan[] = [];
  ai: AiResponse | null = null;
  error = '';
  loading = false;

  constructor(private http: HttpClient) {}

  login() {
    this.error = '';
    this.http.post<any>(`${this.gateway}/api/v1/auth/login`,
      { username: this.username, password: this.password })
      .subscribe({
        next: r => { this.token = r.accessToken; this.loadLoans(); },
        error: e => this.error = e.error?.message || 'Login failed'
      });
  }

  ask() {
    if (!this.question.trim()) return;
    this.loading = true; this.error = '';
    this.http.post<AiResponse>(`${this.gateway}/api/v1/ai/ask`,
      { question: this.question }, { headers: this.authHeaders() })
      .subscribe({
        next: r => { this.ai = r; this.loading = false; },
        error: e => { this.error = e.error?.message || 'AI request failed'; this.loading = false; }
      });
  }

  loadLoans() {
    this.http.get<Loan[]>(`${this.gateway}/api/v1/banking/loans`,
      { headers: this.authHeaders() })
      .subscribe({ next: r => this.loans = r, error: () => this.error = 'Unable to load banking data' });
  }

  logout() { this.token = ''; this.ai = null; this.loans = []; }

  private authHeaders() {
    return new HttpHeaders({ Authorization: `Bearer ${this.token}` });
  }
}
