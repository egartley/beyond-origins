from typing import List
from pygame import Surface

from src.engine.animation import Animation
from src.engine.sprite import Sprite


class LevelEntity(Sprite):

    def __init__(self, width: int, height: int):
        super().__init__(width, height)
        self.rel_x, self.rel_y = 0.0, 0.0
        self.speed = 100
        self.up, self.down, self.left, self.right = False, False, False, False
        self.animation = None
        self.animations = []
        self.current_animation_index = 0

    def _sync_image_rect(self, image: Surface):
        self.image = image
        self.rect.width = self.image.get_width()
        self.rect.height = self.image.get_height()

    def add_animations(self, animations: List[Animation]):
        for a in animations:
            self.animations.append(a)
        self.animation = self.animations[self.current_animation_index]
        self._sync_image_rect(self.animation.frames[self.animation.index])

    def set_animation(self, i: int):
        if 0 <= i < len(self.animations):
            self.animation.stop()
            self.current_animation_index = i
            self.animation = self.animations[self.current_animation_index]
            self.animation.start()
            self._sync_image_rect(self.animation.frames[self.animation.index])

    def tick(self, delta: float):
        super().tick(delta)
        if self.animation.frame is not None and self.animation.frame is not self.image:
            self._sync_image_rect(self.animation.frame)
