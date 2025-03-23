from typing import List

from pygame import Surface
from pygame.sprite import Sprite
from src.engine.animation import Animation


class AnimatedSprite(Sprite):

    def __init__(self, animations: List[Animation]):
        super().__init__()
        self.animations = animations
        self.animation = self.animations[0]
        self.image = self.animation.get_frame()
        self.rect = self.image.get_rect()

    def tick(self):
        frame = self.animation.get_frame()
        if self.image != frame:
            self.image = frame

    def render(self) -> Surface:
        return self.image

    def set_animation(self, index: int):
        if 0 <= index < len(self.animations):
            self.animation = self.animations[index]
