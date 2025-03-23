import pygame.image

from src.engine.animation import Animation
from src.engine.event import EventStore
from src.engine.sprite import AnimatedSprite


class Player(AnimatedSprite):

    def __init__(self, es: EventStore):
        full_sheet = pygame.image.load("res/images/entities/player-default.png")
        full_sheet.convert_alpha()
        left_sheet = full_sheet.subsurface((0, 0, 120, 44))
        right_sheet = full_sheet.subsurface((0, 44, 120, 44))
        left = Animation(es, 200, left_sheet, 4, True)
        right = Animation(es, 200, right_sheet, 4, True)

        super().__init__([left, right])
        self.set_animation(1)

    def tick(self):
        super().tick()
        if not self.animation.is_running:
            self.animation.start()
