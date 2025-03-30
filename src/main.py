import copy
import pygame
import sys

from src.states.in_game import InGameState


def main():
    pygame.init()
    pygame.display.set_mode((1920, 1080))
    pygame.display.set_caption("Beyond Origins")
    pygame.display.set_icon(pygame.image.load("res/images/favicon.png"))
    clock = pygame.time.Clock()
    running = True

    screen = pygame.display.get_surface()
    in_game = InGameState()
    current_state = in_game
    old_rects = [None]

    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
            elif event.type == pygame.KEYUP or event.type == pygame.KEYDOWN:
                current_state.ks.process_key(event)
            elif event.type >= pygame.USEREVENT:
                current_state.es.process_event(event.type)

        delta = clock.tick(60) / 1000  # decouple frame rate from per-frame updates
        current_state.tick(delta)
        new_rects = [r for r in current_state.render(screen) if r is not None]
        pygame.display.update(old_rects + new_rects)
        # print(f"{old_rects}, {new_rects}")
        old_rects = copy.deepcopy(new_rects)  # monitor for performance with lots of rects

    pygame.quit()
    sys.exit()

if __name__ == "__main__":
    main()
