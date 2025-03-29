import unittest
from unittest.mock import Mock
import pygame

from src.engine.event import EventStore, EventHook


class TestEventStore(unittest.TestCase):

    def setUp(self):
        self.event_store = EventStore()
        self.event_hook = Mock(spec=EventHook)

    def test_add_event(self):
        event_id = self.event_store.add_event(self.event_hook, delay=1000, loops=1)
        self.assertIn(event_id, self.event_store.events)

    def test_remove_event(self):
        event_id = self.event_store.add_event(self.event_hook, delay=1000, loops=1)
        self.event_store.remove_event(event_id)
        self.assertNotIn(event_id, self.event_store.events)
        with self.assertRaises(ValueError):
            self.event_store.remove_event(event_id)

    def test_start_event(self):
        event_id = self.event_store.add_event(self.event_hook, delay=1000, loops=1)
        with self.assertRaises(pygame.error):
            self.event_store.start_event(event_id)
        with self.assertRaises(ValueError):
            self.event_store.start_event(9999)

    def test_stop_event(self):
        event_id = self.event_store.add_event(self.event_hook, delay=1000, loops=1)
        with self.assertRaises(pygame.error):
            self.event_store.stop_event(event_id)
        with self.assertRaises(ValueError):
            self.event_store.stop_event(9999)

    def test_process_event(self):
        event_id = self.event_store.add_event(self.event_hook, delay=1000, loops=1)
        self.event_store.process_event(event_id)
        self.event_hook.event_triggered.assert_called_once()
        with self.assertRaises(ValueError):
            self.event_store.remove_event(event_id)  # one-time event should already be removed

    def test_get_event(self):
        event_id = self.event_store.add_event(self.event_hook, delay=1000, loops=1)
        event = self.event_store.get_event(event_id)
        self.assertIsNotNone(event)
        self.assertEqual(event[1], 1000)
        self.assertIsNone(self.event_store.get_event(9999))


if __name__ == '__main__':
    unittest.main()