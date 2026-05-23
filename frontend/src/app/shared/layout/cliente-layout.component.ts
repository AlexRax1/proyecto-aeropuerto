import { Component } from '@angular/core';

import {
  RouterLink,
  RouterOutlet
} from '@angular/router';

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

}
