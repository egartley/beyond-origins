import math

from src.engine.event import EventHook, EventStore


def oscillate(t, min_val, max_val):
    amplitude = (max_val - min_val) / 2
    midpoint = (max_val + min_val) / 2
    return amplitude * math.sin(t) + midpoint


class Oscillator(EventHook):

    def __init__(self, es: EventStore, min_val: int=-10, max_val: int=10, duration: int=500):
        self.es, self.min_val, self.max_val = es, min_val, max_val
        self.duration = duration
        self.event_id = self.es.add_event(self, duration, 0)
        self.t, self.value = 0, 0
        self.is_running = False

    def start(self):
        if not self.is_running:
            self.t, self.value = 0, 0
            self.es.start_event(self.event_id)
            self.is_running = True

    def stop(self):
        if self.is_running:
            self.t, self.value = 0, 0
            self.es.stop_event(self.event_id)
            self.is_running = False

    def restart(self):
        self.stop()
        self.start()

    def event_triggered(self):
        self.t += math.pi * ((self.duration / 1000) / 1)
        self.value = oscillate(self.t, self.min_val, self.max_val)
