import {Component, OnInit, ViewChild} from '@angular/core';
import { ActivatedRoute, Router} from '@angular/router';
import { HttpClient} from '@angular/common/http';
import { DomSanitizer} from '@angular/platform-browser';
import {MatPaginator, MatSnackBar, MatTableDataSource} from '@angular/material';
import {FormControl} from '@angular/forms';
import { FieldService } from '../shared/field/field.service';

export interface Index {
  value: string;
  viewValue: string;
}
export interface Type {
  value: string;
  viewValue: string;
}
@Component({
  selector: 'app-query2',
  templateUrl: './query2.component.html',
  styleUrls: ['./query2.component.css']
})
export class Query2Component implements OnInit {
  // tslint:disable-next-line:ban-types
  http: String = 'http://localhost:8080/logcollector/';
  loading: boolean;
  inputData: any = {
    Index: 'audit*',

    Is1: false,
    Key1: 'USER_ID',
    Value1: '',

    Type1: '',
    Is2: false,
    Key2: '',
    Value2: '',

    Type2: '',
    Is3: false,
    Key3: '',
    Value3: '',

    Type3: '',
    Is4: false,
    Key4: '',
    Value4: '',

    Type4: '',
    Is5: false,
    Key5: '',
    Value5: '',


    Daygte: '',
    Timegte: '',
    Daylte: '',
    Timelte: '',

    selectget: '',


  };


  constructor(private httpClient: HttpClient, private snackBar: MatSnackBar , private fieldService: FieldService, private sanitizer: DomSanitizer) {
    this.sanitizer = sanitizer;
  }

  ngOnInit() {
    this.loading = false;
  }

  savescorrelation() {
    if (this.loading === false ) {
      this.loading = true;
      this.httpClient.post(this.http + 'saveCorrelationID' , this.inputData)
        .subscribe(data => {
            console.log('EXPORT BY CORRELATION_ID OK', data);
            this.snackBar.open('COMPLETE', 'OK', {duration: 3000});
            this.loading = false;
            this.inputData.Value1 = '';
            this.inputData.Daygte = '';
            this.inputData.Timegte = '';
            this.inputData.Daylte = '';
            this.inputData.Timelte = '';
          }, error => {
            if (error.error.message === 'please insert value 1') {
              this.snackBar.open('please insert USER_ID', 'OK', {duration: 3000});
            } else {
              this.snackBar.open(error.error.message, 'OK', {duration: 3000});
            }

            console.log(error);
            this.loading = false;
          }
        );
    } else {
      this.snackBar.open('BUSY', 'OK', {duration: 3000});
    }
  }


}
