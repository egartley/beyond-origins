import math

from src.engine.event import EventHook, EventStore
from src.engine.sprite import Sprite


def sine_oscillator(t, min_val, max_val):
    amplitude = (max_val - min_val) / 2
    midpoint = (max_val + min_val) / 2
    return amplitude * math.sin(t) + midpoint


class HoverControl(EventHook):

    def __init__(self, es: EventStore, sprite: Sprite, span: int=10, duration: int=500):
        self.es = es
        self.sprite = sprite
        self.span = span
        self.duration = duration
        self.event_id = self.es.add_event(self, duration, 0)
        self.t = 0
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
        self.sprite.y_offset = sine_oscillator(self.t, -self.span, self.span)
