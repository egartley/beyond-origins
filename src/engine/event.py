import pygame.time


class EventHook:

    def event_triggered(self):
        pass


class EventStore:

    def __init__(self):
        self.events = []

    def add_event(self, hook: EventHook, delay: int, loops: int = 0):
        event_id = pygame.event.custom_type()
        self.events.append((event_id, hook, delay, loops))
        return event_id

    def remove_event(self, event_id: int):
        for e in self.events:
            if e[0] == event_id:
                self.events.remove(e)
                break

    def clear_events(self):
        self.events = []

    def start_event(self, event_id: int):
        event = self.get_event(event_id)
        if event is None:
            raise Exception(f"Unable to start event with invalid id \"{event_id}\"")
        pygame.time.set_timer(event_id, event[2], event[3])

    def stop_event(self, event_id: int):
        event = self.get_event(event_id)
        if event is None:
            raise Exception(f"Unable to stop event with invalid id \"{event_id}\"")
        else:
            pygame.time.set_timer(event_id, 0)

    def get_event(self, event_id: int) -> tuple[int, EventHook, int, int] | None:
        for e in self.events:
            if e[0] == event_id:
                return e
        return None

    def process_event(self, event_id):
        event = self.get_event(event_id)
        if event is not None:
            event[1].event_triggered()
