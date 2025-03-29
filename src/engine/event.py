from collections import defaultdict

import pygame.time


class EventHook:

    def event_triggered(self):
        pass


class EventStore:

    def __init__(self):
        self.events = defaultdict(lambda: tuple())

    def add_event(self, hook: EventHook, delay: int, loops: int = 0) -> int:
        event_id = pygame.event.custom_type()
        if event_id in self.events:
            raise ValueError(f"Cannot add event id \"{event_id}\" since it already exists")
        if event_id <= pygame.USEREVENT:
            raise ValueError(f"Generated event id \"{event_id}\" is not greater than userevent")
        self.events[event_id] = (hook, delay, loops)
        return event_id

    def remove_event(self, event_id: int):
        if self.events.pop(event_id, None) is None:
            raise ValueError(f"Unable to remove unknown event ID \"{event_id}\"")

    def start_event(self, event_id: int):
        event = self.get_event(event_id)
        if event is None:
            raise ValueError(f"Unable to start event with unknown id \"{event_id}\"")
        pygame.time.set_timer(event_id, event[1], event[2])

    def stop_event(self, event_id: int):
        event = self.get_event(event_id)
        if event is None:
            raise ValueError(f"Unable to stop event with unknown id \"{event_id}\"")
        else:
            pygame.time.set_timer(event_id, 0)

    def get_event(self, event_id: int) -> tuple[EventHook, int, int] | None:
        return self.events.get(event_id)

    def process_event(self, event_id):
        event = self.get_event(event_id)
        if event is not None:
            event[0].event_triggered()
            if event[2] == 1:  # remove one-time events after they're triggered
                self.remove_event(event_id)
