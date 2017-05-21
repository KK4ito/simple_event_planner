import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'duration'})
export class DurationPipe implements PipeTransform {
  transform(time: number): string {
    let seconds = time % 60;
    let minutes = (Math.round(time / 60) % 60);
    let hours = Math.round((time / (60 * 60)));

    let hoursStr = hours.toString();
    let minutesStr = (minutes <= 9) ? '0' + minutes : minutes.toString()

    return hoursStr + ':' + minutesStr;
  }
}
