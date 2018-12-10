import {Component, OnInit} from '@angular/core';
import {addDays, isSameDay, isSameMonth} from 'date-fns';
import {Subject} from 'rxjs';
import {CalendarEvent, CalendarEventTimesChangedEvent} from 'angular-calendar';
import {DAYS_OF_WEEK} from 'calendar-utils';
import {Italian} from 'flatpickr/dist/l10n/it';
import {DeadlinesService} from './deadlines.service';

const colors: any = {
  red: {primary: '#ad2121', secondary: '#ff6666'},
  blue: {primary: '#1e90ff', secondary: '#78bcff'},
  yellow: {primary: '#e3bc08', secondary: '#FDF1BA'}
};

@Component({
  selector: 'app-deadlines',
  templateUrl: './deadlines.component.html',
  styleUrls: ['./deadlines.component.css'],
})

export class DeadlinesComponent implements OnInit {
  selEvent = undefined;
  view = 'month';
  viewDate: Date = new Date();
  weekStartsOn: number = DAYS_OF_WEEK.MONDAY;
  locale = Italian;
  eventOpen = false;
  refresh: Subject<any> = new Subject();
  events: CalendarEvent[] = [];
  activeDayIsOpen = this.events.length > 0 && this.events.some(function (ev) {
    return ev.start.toDateString() === new Date().toDateString();
  });

  constructor(public deadlinesService: DeadlinesService) {
  }

  ngOnInit(): void {
    this.getAllItems(false);
  }

  dayClicked({date, events}: { date: Date; events: CalendarEvent[] }): void {
    if (isSameMonth(date, this.viewDate)) {
      this.viewDate = date;
      this.activeDayIsOpen = !((isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) ||
        events.length === 0);
    }
  }

  eventTimesChanged({
                      event,
                      newStart
                    }: CalendarEventTimesChangedEvent): void {
    event.start = newStart;
    this.refresh.next();
  }

  showDetails(event) {
    if (!this.selEvent) {
      this.eventOpen = true;
      const selEventArr = this.events.filter(function (ev) {
        if (ev.id === event.id) {
          return ev;
        }
      });
      if (selEventArr && selEventArr.length === 1) {
        this.selEvent = selEventArr[0];
      }
    } else {
      this.selEvent = undefined;
      this.eventOpen = false;
      this.getAllItems(true);
    }
  }

  addEvent(): void {
    this.events.push({
      title: 'Nuovo evento',
      start: new Date(),
      allDay: true
    });
    const lastEv = this.events.pop();
    this.save(lastEv, false);
    this.selEvent = lastEv;
    this.refresh.next();
  }

  updateEv(event): void {
    event.start = new Date(event.start);
    this.save(event, true);
    this.refresh.next();
  }

  save(ev, resetEv): void {
    delete ev.color;
    this.deadlinesService.addEvent(ev).then(newEv => {
      this.getAllItems(resetEv);
      this.selEvent = newEv;
    }).catch(error => {
      console.error(error);
    });
  }

  getAllItems(resetEv): void {
    this.deadlinesService.getAll().subscribe(
      restItems => {
        restItems.forEach(ev => {
          ev.start = new Date(ev.start);
          ev.color = ev.paid ? colors.blue : colors.red;
          ev.allDay = true;
        });
        this.events = restItems;
        if (resetEv) {
          this.selEvent = undefined;
        }
      }
    );
  }

  deleteFunc(event): void {
    this.deadlinesService.deleteEvent(event).then(_ => {
      this.events.splice(event.id, 1);
      this.getAllItems(true);
      this.refresh.next();
    }).catch(error => {
      console.error(error);
    });
  }

  postponeEv(value, event): void {
    event.start = addDays(event.start, value);
    this.updateEv(event);
  }
}
