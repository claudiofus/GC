import {Component, OnInit} from '@angular/core';
import {addDays, addHours, endOfDay, endOfMonth, isSameDay, isSameMonth, startOfDay, subDays} from 'date-fns';
import {Subject} from 'rxjs';
import {CalendarEvent, CalendarEventTimesChangedEvent} from 'angular-calendar';
import {DAYS_OF_WEEK} from 'calendar-utils';
import {Italian} from 'flatpickr/dist/l10n/it';
import {DeadlinesService} from './deadlines.service';

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

  dayClicked({date, events}: { date: Date; events: CalendarEvent[] }): void {
    if (isSameMonth(date, this.viewDate)) {
      this.viewDate = date;
      this.activeDayIsOpen = !((isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) ||
        events.length === 0);
    }
  }

  eventTimesChanged({
                      event,
                      newStart,
                      newEnd
                    }: CalendarEventTimesChangedEvent): void {
    event.start = newStart;
    event.end = newEnd;
    this.refresh.next();
  }

  addEvent(): void {
    this.events.push({
      title: 'Nuovo evento',
      start: startOfDay(new Date()),
      end: endOfDay(new Date()),
      color: {primary: '#ad2121', secondary: '#FAE3E3'},
      draggable: true,
      resizable: {
        beforeStart: true,
        afterEnd: true
      }
    });
    this.refresh.next();
  }

  loadEvent(ev): void {
    this.events.push({
      title: ev.title,
      start: ev.start,
      end: ev.end,
      draggable: true,
      resizable: {
        beforeStart: true,
        afterEnd: true
      }
    });
    this.refresh.next();
  }

  save(ev): void {
    delete ev.color;
    delete ev.resizable;
    delete ev.draggable;
    this.deadlinesService.addEvent(ev).then(_ => {
      console.log('ok');
    }).catch(error => {
      console.error(error);
    });
  }

  constructor(public deadlinesService: DeadlinesService) {
  }

  ngOnInit(): void {
    this.deadlinesService.getAll().subscribe(
      restItems => {
        restItems.map(ev => this.loadEvent(ev));
      }
    );
  }
}
