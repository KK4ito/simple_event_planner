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

  /**
   * Food Type
   */
  food: FoodType;

  /**
   * Type of drink
   */
  drink: boolean;

  constructor(private viewCtrl: ViewController) {
  }

  /**
   * Dismiss the current modal
   */
  dismiss() {
    this.viewCtrl.dismiss({ drink: false, selectedFood: FoodType.NONE });
  }

  /**
   * Dismiss the current modal and return the drink/food
   */
  save() {
    this.viewCtrl.dismiss({ drink: this.drink, selectedFood: this.food });
  }
}
