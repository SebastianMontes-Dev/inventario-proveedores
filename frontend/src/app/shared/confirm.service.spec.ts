import { TestBed } from '@angular/core/testing';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { of } from 'rxjs';
import { ConfirmService } from './confirm.service';

describe('ConfirmService', () => {
  let service: ConfirmService;
  let dialog: MatDialog;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MatDialogModule, NoopAnimationsModule],
      providers: [ConfirmService]
    });
    service = TestBed.inject(ConfirmService);
    dialog = TestBed.inject(MatDialog);
  });

  it('debe ser creado', () => {
    expect(service).toBeTruthy();
  });

  it('debe abrir dialogo de confirmacion y resolver true al confirmar', async () => {
    const dialogRef = { afterClosed: () => of(true) };
    const spy = jest.spyOn(dialog, 'open').mockReturnValue(dialogRef as any);

    const result = await service.confirm({ message: 'Eliminar producto?' });

    expect(result).toBe(true);
    expect(spy).toHaveBeenCalledWith(
      expect.any(Function),
      expect.objectContaining({
        width: '420px',
        data: expect.objectContaining({ message: 'Eliminar producto?' })
      })
    );
  });

  it('debe resolver false al cancelar dialogo', async () => {
    const dialogRef = { afterClosed: () => of(false) };
    jest.spyOn(dialog, 'open').mockReturnValue(dialogRef as any);

    const result = await service.confirm({ message: 'Seguro?' });

    expect(result).toBe(false);
  });

  it('debe pasar opciones completas al dialogo', async () => {
    const dialogRef = { afterClosed: () => of(true) };
    const spy = jest.spyOn(dialog, 'open').mockReturnValue(dialogRef as any);

    await service.confirm({
      title: 'Confirmar eliminacion',
      message: 'Esta accion no se puede deshacer',
      confirmLabel: 'Si, eliminar',
      cancelLabel: 'Cancelar',
      variant: 'danger'
    });

    expect(spy).toHaveBeenCalledWith(
      expect.any(Function),
      expect.objectContaining({
        data: expect.objectContaining({
          title: 'Confirmar eliminacion',
          message: 'Esta accion no se puede deshacer',
          confirmLabel: 'Si, eliminar',
          cancelLabel: 'Cancelar',
          variant: 'danger'
        })
      })
    );
  });
});
