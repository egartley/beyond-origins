import pygame.sprite
from pygame import Surface, Rect


class Sprite(pygame.sprite.Sprite):

    def __init__(self, width: int = 32, height: int = 32):
        super().__init__()
        self.x, self.y = 0.0, 0.0
        self.x_offset, self.y_offset = 0.0, 0.0
        self.image = None
        self.rect = Rect(0, 0, width, height)

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

    def render(self, surface: Surface):
        # pygame.draw.rect(surface, (255, 255, 255), self.rect, 1)
        surface.blit(self.image, (int(self.x + self.x_offset), int(self.y + self.y_offset)))
