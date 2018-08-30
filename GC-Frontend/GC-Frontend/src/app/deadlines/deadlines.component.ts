import {Component, OnInit} from '@angular/core';
import {isSameDay, isSameMonth} from 'date-fns';
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
    this.save(this.events.pop());
    this.refresh.next();
  }

  updateEv(event): void {
    event.start = new Date(event.start);
    this.save(event);
    this.refresh.next();
  }

  save(ev): void {
    this.deadlinesService.addEvent(ev).then(_ => {
      this.getAllItems();
    }).catch(error => {
      console.error(error);
    });
  }

  getAllItems(): void {
    this.deadlinesService.getAll().subscribe(
      restItems => {
        restItems.forEach(ev => ev.start = new Date(ev.start));
        this.events = restItems;
      }
    );
  }

  deleteFunc(index): void {
    this.deadlinesService.deleteEvent(this.events[index]).then(_ => {
      this.events.splice(index, 1);
      this.getAllItems();
      this.refresh.next();
    }).catch(error => {
      console.error(error);
    });
  }

  constructor(public deadlinesService: DeadlinesService) {
  }

  ngOnInit(): void {
    this.getAllItems();
  }
}
