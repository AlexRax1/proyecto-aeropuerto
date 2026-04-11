<<<<<<< HEAD
import { Component } from '@angular/core';
=======
import { Component, signal } from '@angular/core';
>>>>>>> 1e84a6313c7645cbdfa6e0d5f4f2b3539a6d4e8b
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
<<<<<<< HEAD
  standalone: true,
  imports: [RouterOutlet], 
  template: `<router-outlet></router-outlet>` 
})
export class AppComponent {}
=======
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');
}
>>>>>>> 1e84a6313c7645cbdfa6e0d5f4f2b3539a6d4e8b
