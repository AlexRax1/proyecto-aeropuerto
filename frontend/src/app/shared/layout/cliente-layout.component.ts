import { Component } from '@angular/core';

import {
  Router,
  RouterLink,
  RouterOutlet
} from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-cliente-layout',

  standalone: true,

  imports: [
    RouterLink,
    RouterOutlet
  ],

  templateUrl:
    './cliente-layout.component.html',

  styleUrls:
    ['./cliente-layout.component.css']
})
export class ClienteLayoutComponent {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  logout() {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
