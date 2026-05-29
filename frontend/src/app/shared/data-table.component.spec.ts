import { Component } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { DataTableComponent, TableColumn } from './data-table.component';

interface TestRow { id: number; nombre: string; cantidad: number; }

@Component({
  standalone: true,
  imports: [DataTableComponent],
  template: `
    <app-data-table [columns]="columns" [rows]="rows" [loading]="loading" [paginator]="false">
    </app-data-table>
  `
})
class TestHostComponent {
  columns: TableColumn<TestRow>[] = [
    { key: 'id', header: 'ID' },
    { key: 'nombre', header: 'Nombre', sortable: true },
    { key: 'cantidad', header: 'Cantidad', sortable: true, align: 'end' }
  ];
  rows: TestRow[] = [];
  loading = false;
}

describe('DataTableComponent', () => {
  let fixture: ComponentFixture<TestHostComponent>;
  let host: TestHostComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        DataTableComponent,
        TestHostComponent,
        MatTableModule,
        MatSortModule,
        MatPaginatorModule,
        NoopAnimationsModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(TestHostComponent);
    host = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('debe mostrar datos en la tabla', () => {
    host.rows = [
      { id: 1, nombre: 'Producto A', cantidad: 10 },
      { id: 2, nombre: 'Producto B', cantidad: 5 }
    ];
    fixture.detectChanges();

    const rows = fixture.nativeElement.querySelectorAll('tr.mat-mdc-row');
    expect(rows.length).toBe(2);
  });

  it('debe mostrar skeleton loader mientras esta cargando', () => {
    host.loading = true;
    host.rows = [];
    fixture.detectChanges();

    const skeleton = fixture.nativeElement.querySelector('app-skeleton-table');
    expect(skeleton).toBeTruthy();
  });

  it('debe mostrar estado vacio cuando no hay filas', () => {
    host.loading = false;
    host.rows = [];
    fixture.detectChanges();

    const empty = fixture.nativeElement.querySelector('app-empty-state');
    expect(empty).toBeTruthy();
  });

  it('debe mostrar encabezados de columna correctos', () => {
    host.rows = [{ id: 1, nombre: 'Test', cantidad: 0 }];
    fixture.detectChanges();

    const headers = fixture.nativeElement.querySelectorAll('th.mat-mdc-header-cell');
    const headerTexts = Array.from(headers).map((th: any) => th.textContent.trim());
    expect(headerTexts).toEqual(['ID', 'Nombre', 'Cantidad']);
  });

  xit('debe emitir page al cambiar pagina', () => {
  });
});
