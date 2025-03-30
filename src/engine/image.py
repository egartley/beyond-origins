from collections import defaultdict
from enum import Enum

import pygame.image
from pygame import Surface


class ImageStore:

    def __init__(self):
        self.images = defaultdict(None)

    def clear(self):
        self.images.clear()

    def get(self, identifier: Enum) -> Surface:
        if identifier in self.images:
            return self.images[identifier]
        return self._load(identifier)

    def _load(self, identifier: Enum) -> Surface:
        image = pygame.image.load(identifier.value)
        image.convert_alpha()
        self.images[identifier] = image
        return image
