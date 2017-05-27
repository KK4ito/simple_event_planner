import { Component } from '@angular/core';
import { ViewController } from 'ionic-angular';
import { FoodType } from "../../models/FoodType";

@Component({
  selector: 'page-select-food',
  templateUrl: 'select-food.html',
})
export class SelectFoodPage {
  menus = [
    {'value': 'VEGI', 'icon': 'nutrition'},
    {'value': 'MEAT', 'icon': 'restaurant'},
    {'value': 'NONE', 'icon': 'remove-circle'}
  ];

  food: FoodType;
  drink: boolean;

  constructor(private viewCtrl: ViewController) {
  }

  dismiss() {
    this.viewCtrl.dismiss({ drink: false, selectedFood: FoodType.NONE });
  }

  save() {
    this.viewCtrl.dismiss({ drink: this.drink, selectedFood: this.food });
  }
}
