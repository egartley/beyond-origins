from pygame import Surface

from src.engine.event import EventStore, EventHook


class Animation(EventHook):

    def __init__(self, es: EventStore, frame_time: int, sheet: Surface, num_frames: int, loop: bool):
        self.es = es
        self.frame_time = frame_time
        self.sheet = sheet
        self.num_frames = num_frames
        self.is_looping = loop

        self.index = 0
        self.frames = []
        self.is_running = False
        self.event_id = self.es.add_event(self, self.frame_time, 0 if self.is_looping else 1)
        self.build_frames()

    def build_frames(self):
        w = self.sheet.get_width() / self.num_frames
        h = self.sheet.get_height()
        for n in range(self.num_frames):
            self.frames.append(self.sheet.subsurface((n * w, 0, w, h)))

    def get_frame(self) -> Surface | None:
        if len(self.frames) == 0 or self.index < 0:
            return None
        return self.frames[self.index]

    def start(self):
        self.es.start_event(self.event_id)
        self.is_running = True

    def stop(self):
        self.es.stop_event(self.event_id)
        self.is_running = False

    def restart(self):
        self.index = 0
        if not self.is_running:
            self.start()

    def event_triggered(self):
        if not self.is_running:
            return
        self.index += 1
        if self.index == len(self.frames):
            if self.is_looping:
                self.index = 0
            else:
                self.index = len(self.frames) - 1
                self.stop()
