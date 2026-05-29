import { TestBed } from '@angular/core/testing';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { NotifyService } from './notify.service';

describe('NotifyService', () => {
  let service: NotifyService;
  let snackBar: MatSnackBar;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MatSnackBarModule, NoopAnimationsModule],
      providers: [NotifyService]
    });
    service = TestBed.inject(NotifyService);
    snackBar = TestBed.inject(MatSnackBar);
  });

  it('debe ser creado', () => {
    expect(service).toBeTruthy();
  });

  it('debe abrir snackbar con clase success al llamar success()', () => {
    const spy = jest.spyOn(snackBar, 'open');
    service.success('Operacion exitosa');
    expect(spy).toHaveBeenCalledWith(
      'Operacion exitosa',
      'Cerrar',
      expect.objectContaining({
        panelClass: ['app-notify', 'notify-success'],
        duration: 3000,
        verticalPosition: 'top',
        horizontalPosition: 'center'
      })
    );
  });

  it('debe abrir snackbar con clase error al llamar error()', () => {
    const spy = jest.spyOn(snackBar, 'open');
    service.error('Error critico');
    expect(spy).toHaveBeenCalledWith(
      'Error critico',
      'Cerrar',
      expect.objectContaining({
        panelClass: ['app-notify', 'notify-error'],
        duration: 4500
      })
    );
  });

  it('debe abrir snackbar con clase warning al llamar warning()', () => {
    const spy = jest.spyOn(snackBar, 'open');
    service.warning('Atencion requerida');
    expect(spy).toHaveBeenCalledWith(
      'Atencion requerida',
      'Cerrar',
      expect.objectContaining({
        panelClass: ['app-notify', 'notify-warning'],
        duration: 4000
      })
    );
  });

  it('debe abrir snackbar con clase info al llamar info()', () => {
    const spy = jest.spyOn(snackBar, 'open');
    service.info('Informacion');
    expect(spy).toHaveBeenCalledWith(
      'Informacion',
      'Cerrar',
      expect.objectContaining({
        panelClass: ['app-notify', 'notify-info'],
        duration: 3000
      })
    );
  });

  it('debe permitir action personalizado', () => {
    const spy = jest.spyOn(snackBar, 'open');
    service.success('Guardado', 'Deshacer');
    expect(spy).toHaveBeenCalledWith(
      'Guardado',
      'Deshacer',
      expect.any(Object)
    );
  });

  it('debe combinar panelClass del config con las clases base', () => {
    const spy = jest.spyOn(snackBar, 'open');
    service.success('Mensaje', 'Cerrar', { panelClass: ['extra-class'] });
    expect(spy).toHaveBeenCalledWith(
      'Mensaje',
      'Cerrar',
      expect.objectContaining({
        panelClass: ['app-notify', 'notify-success', 'extra-class']
      })
    );
  });
});
