import math

from src.engine.event import EventHook, EventStore


def oscillate(t, min_val, max_val):
    amplitude = (max_val - min_val) / 2
    midpoint = (max_val + min_val) / 2
    return amplitude * math.sin(t) + midpoint


class HoverControl(EventHook):

    def __init__(self, es: EventStore, span: int=10, duration: int=500):
        self.es, self.span = es, span
        self.duration = duration
        self.event_id = self.es.add_event(self, duration, 0)
        self.t, self.value = 0, 0
        self.is_running = False

    def start(self):
        if not self.is_running:
            self.t = 0
            self.es.start_event(self.event_id)
            self.is_running = True

    def stop(self):
        if self.is_running:
            self.t = 0
            self.es.stop_event(self.event_id)
            self.is_running = False

    def restart(self):
        self.stop()
        self.start()

    def event_triggered(self):
        self.t += math.pi * ((self.duration / 1000) / 1)
        self.value = oscillate(self.t, -self.span, self.span)
