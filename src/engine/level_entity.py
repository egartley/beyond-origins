import math
from typing import List
from pygame import Surface

from src.engine.animation import Animation
from src.engine.sprite import Sprite


class LevelEntity(Sprite):

    def __init__(self, width: int = 32, height: int = 32):
        super().__init__(width, height)
        self.rel_x, self.rel_y = 0.0, 0.0
        self.vel_x, self.vel_y = 0.0, 0.0
        self.speed, self.accel, self.decel = 200, 300, 200
        self.up, self.down, self.left, self.right = False, False, False, False
        self.frozen = False
        self.animation = None
        self.animations = []
        self.current_animation_index = 0

    def _sync_image_rect(self, image: Surface):
        self.image = image
        self.rect.width = self.image.get_width()
        self.rect.height = self.image.get_height()

    def move_x(self, delta: float):
        self.x += self.vel_x * delta
        self.rect.x = int(self.x)

    def move_y(self, delta: float):
        self.y += self.vel_y * delta
        self.rect.y = int(self.y)

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
        if self.animation.frame is not None and self.animation.frame is not self.image:
            self._sync_image_rect(self.animation.frame)
        if self.up:
            self.vel_y -= self.accel * delta
        if self.down:
            self.vel_y += self.accel * delta
        if self.left:
            self.vel_x -= self.accel * delta
        if self.right:
            self.vel_x += self.accel * delta
        if not self.up and not self.down:
            if self.vel_y > 0:
                self.vel_y -= self.decel * delta
                self.vel_y = max(0, self.vel_y)
            elif self.vel_y < 0:
                self.vel_y += self.decel * delta
                self.vel_y = min(0, self.vel_y)
        if not self.left and not self.right:
            if self.vel_x > 0:
                self.vel_x -= self.decel * delta
                self.vel_x = max(0, self.vel_x)
            elif self.vel_x < 0:
                self.vel_x += self.decel * delta
                self.vel_x = min(0, self.vel_x)
        c = math.sqrt(self.vel_x**2 + self.vel_y**2)
        if c > self.speed:
            scale = self.speed / c
            self.vel_x *= scale
            self.vel_y *= scale
