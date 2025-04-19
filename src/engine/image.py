from collections import defaultdict
from enum import Enum
from typing import Any

import pygame.image
from pygame import Surface


class ImageStore:

    def __init__(self):
        self.images = defaultdict(None)

    def clear(self):
        self.images.clear()

    def get(self, identifier: Any, alpha: bool = False) -> Surface:
        if identifier in self.images:
            return self.images[identifier]
        return self._load(identifier, alpha)

    def _load(self, identifier: Any, alpha: bool) -> Surface:
        i = identifier.value if isinstance(identifier, Enum) else identifier
        image = pygame.image.load(i)
        if alpha:
            image.convert_alpha()
        else:
            image.convert()
        self.images[i] = image
        return image
