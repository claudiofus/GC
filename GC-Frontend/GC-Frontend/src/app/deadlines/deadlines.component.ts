import {Component, OnInit} from '@angular/core';
import {addDays, isSameDay, isSameMonth} from 'date-fns';
import {Subject} from 'rxjs';
import {CalendarEvent, CalendarEventTimesChangedEvent} from 'angular-calendar';
import {DAYS_OF_WEEK} from 'calendar-utils';
import {Italian} from 'flatpickr/dist/l10n/it';
import {DeadlinesService} from './deadlines.service';

const colors: any = {
  red: {primary: '#ad2121', secondary: '#FAE3E3'},
  blue: {primary: '#1e90ff', secondary: '#D1E8FF'},
  yellow: {primary: '#e3bc08', secondary: '#FDF1BA'}
};

@Component({
  selector: 'app-deadlines',
  templateUrl: './deadlines.component.html',
  styleUrls: ['./deadlines.component.css'],
})

export class DeadlinesComponent implements OnInit {
  showEvents = false;
  view = 'month';
  viewDate: Date = new Date();
  weekStartsOn: number = DAYS_OF_WEEK.MONDAY;
  locale = Italian;

  refresh: Subject<any> = new Subject();
  events: CalendarEvent[] = [];
  activeDayIsOpen = this.events.length > 0 && this.events.some(function (ev) {
    return ev.start.toDateString() === new Date().toDateString();
  });

  constructor(public deadlinesService: DeadlinesService) {
  }

  ngOnInit(): void {
    this.getAllItems();
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

  addEvent(): void {
    this.events.push({
      title: 'Nuovo evento',
      start: new Date()
    });
    const lastEv = this.events.pop();
    this.save(lastEv);
    this.refresh.next();
  }

  updateEv(event): void {
    event.start = new Date(event.start);
    this.save(event);
    this.refresh.next();
  }

  save(ev): void {
    delete ev.color;
    this.deadlinesService.addEvent(ev).then(_ => {
      this.getAllItems();
    }).catch(error => {
      console.error(error);
    });
  }

  getAllItems(): void {
    this.deadlinesService.getAll().subscribe(
      restItems => {
        restItems.forEach(ev => {
          ev.start = new Date(ev.start);
          ev.color = ev.paid ? colors.blue : colors.red;
        });
        this.events = restItems;
      }
    );
  }

  deleteFunc(event): void {
    this.deadlinesService.deleteEvent(event).then(_ => {
      this.events.splice(event.id, 1);
      this.getAllItems();
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
