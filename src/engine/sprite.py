from typing import List

import pygame.sprite
from pygame import Surface, Rect
from src.engine.animation import Animation


class Sprite(pygame.sprite.Sprite):

    def __init__(self, width: int = 32, height: int = 32):
        super().__init__()
        self.x, self.y = 0.0, 0.0
        self.x_offset, self.y_offset = 0.0, 0.0
        self.speed = 100
        self.image = pygame.Surface([width, height])
        self.rect = pygame.rect.Rect(self.x, self.y, width, height)

    def set_position(self, x: float, y: float):
        self.x = x
        self.y = y
        self.rect.move_ip(int(self.x), int(self.y))

    def move_x(self, speed: float, delta: float):
        self.x += speed * delta
        self.rect.x = int(self.x)

    def move_y(self, speed: float, delta: float):
        self.y += speed * delta
        self.rect.y = int(self.y)

    def tick(self, delta: float):
        pass

    def render(self, surface: Surface) -> list[Rect | None]:
        # pygame.draw.rect(surface, (255, 255, 255), self.rect, 1)
        return [surface.blit(self.image, (int(self.x + self.x_offset), int(self.y + self.y_offset)))]


class AnimatedSprite(Sprite):

    def __init__(self, width: int, height: int):
        super().__init__(width, height)
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
