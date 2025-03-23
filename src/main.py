import pygame
import sys

from src.engine.event import EventStore
from src.states.in_game import InGameState


def main():
    pygame.init()
    pygame.display.set_mode((1920, 1080))
    pygame.display.set_caption("Beyond Origins")
    pygame.display.set_icon(pygame.image.load("res/images/favicon.png"))
    clock = pygame.time.Clock()
    running = True

    screen = pygame.display.get_surface()
    w = screen.get_width()
    h = screen.get_height()

    es = EventStore()

    in_game = InGameState(es)
    current_state = in_game

    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
            elif event.type >= pygame.USEREVENT:
                es.process_event(event.type)
        current_state.tick()
        next_frame = current_state.render((w, h))
        screen.blit(next_frame, (0, 0))
        pygame.display.flip()  # TODO: change to more efficient .update(list_rects)
        clock.tick(60)

    pygame.quit()
    sys.exit()

if __name__ == "__main__":
    main()
